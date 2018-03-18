from PIL import ImageGrab
import os
import time
import win32api, win32con
from PIL import ImageOps
from numpy import *
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
import pickle

x_padMainWindow = 0
y_padMainWindow = 0
dic = {}
masterBet = 0.001
NBROUND = 9

VK_CODE = {'backspace':0x08,
           'enter':0x0D,
           'left_arrow':0x25,
           'up_arrow':0x26,
           'right_arrow':0x27,
           'down_arrow':0x28,
           '0':0x30,
           '1':0x31,
           '2':0x32,
           '3':0x33,
           '4':0x34,
           '5':0x35,
           '6':0x36,
           '7':0x37,
           '8':0x38,
           '9':0x39,
           '.':0xBE}

def get_auto():
    box = (x_padMainWindow + dic['auto_top_left'][0], y_padMainWindow + dic['auto_top_left'][1], x_padMainWindow + dic['auto_bottom_right'][0], y_padMainWindow + dic['auto_bottom_right'][1])
    im = ImageOps.grayscale(ImageGrab.grab(box))
    a = array(im.getcolors())
    a = a.sum()
    return a

def get_multiplicateur():
    box = (x_padMainWindow + dic['multiplier_top_left'][0], y_padMainWindow + dic['multiplier_top_left'][1], x_padMainWindow + dic['multiplier_bottom_right'][0], y_padMainWindow + dic['multiplier_bottom_right'][1])
    im = ImageOps.grayscale(ImageGrab.grab(box))
    a = array(im.getcolors())
    a = a.sum()
    return a

def isRoundDone():
    box = (x_padMainWindow + dic['maxBet_top_left'][0], y_padMainWindow + dic['maxBet_top_left'][1], x_padMainWindow + dic['maxBet_bottom_right'][0], y_padMainWindow + dic['maxBet_bottom_right'][1])
    im = ImageOps.grayscale(ImageGrab.grab(box))
    a = array(im.getcolors())
    a = a.sum()
    if (a == dic['gray_max_bet']): #Round is not done
        return False
    return True

def isRoundWon():
    box = (x_padMainWindow + dic['nothing_top_left'][0], y_padMainWindow + dic['nothing_top_left'][1], x_padMainWindow + dic['nothing_bottom_right'][0], y_padMainWindow + dic['nothing_bottom_right'][1])
    im = ImageOps.grayscale(ImageGrab.grab(box))
    a = array(im.getcolors())
    a = a.sum()
    if (a == dic['nothing_lost_value']): #You lost
        return False
    return True

def leftClick():
    win32api.mouse_event(win32con.MOUSEEVENTF_LEFTDOWN, 0, 0)
    time.sleep(.1)
    win32api.mouse_event(win32con.MOUSEEVENTF_LEFTUP, 0, 0)

def mousePos(cord):
    win32api.SetCursorPos((x_padMainWindow + cord[0], y_padMainWindow + cord[1]))

def get_cords():
    x,y = win32api.GetCursorPos()
    x = x - x_padMainWindow
    y = y - y_padMainWindow
    print ("X: ", x, "\tY: ", y)

def setAuto():
    if (get_auto() != dic['auto_on_value']):
        mousePos(dic['auto_click'])
        leftClick()
    if (get_multiplicateur() != dic['multiplier_value']):
        time.sleep(.1)
        while (get_multiplicateur() != dic['multiplier_empty_value']):
            mousePos(dic['multiplier_click'])
            leftClick()
            keyPress('backspace')
        mousePos(dic['multiplier_click'])
        leftClick()
        keyPress('1')
        keyPress('.')
        keyPress('5')
        keyPress('1')

def keyPress(arg):
    win32api.keybd_event(VK_CODE[arg], 0,0,0)
    time.sleep(.1)

def get_site_bet():
    box = (x_padMainWindow + dic['wager_top_left'][0], y_padMainWindow + dic['wager_top_left'][1], x_padMainWindow + dic['wager_bottom_right'][0], y_padMainWindow + dic['wager_bottom_right'][1])
    im = ImageOps.grayscale(ImageGrab.grab(box))
    a = array(im.getcolors())
    a = a.sum()
    return a

def setBet(bet):
    while (get_site_bet() != dic['wager_empty']):
        mousePos(dic['wager_click'])
        leftClick()
        keyPress('backspace')
        time.sleep(.1)
    mousePos(dic['wager_click'])
    leftClick()
    for letter in list(str(bet)):
        keyPress(letter)
    mousePos(dic['enter'])
    #leftClick()

def sendEmail(goodBad, emailBody, emailAddress):
    fromaddr = "wtfskinbot@gmail.com"
    toaddr = emailAddress
    msg = MIMEMultipart()
    msg['From'] = fromaddr
    msg['To'] = toaddr
    msg['Subject'] = goodBad +  " Update"
    
    body = emailBody
    msg.attach(MIMEText(body, 'plain'))
    
    server = smtplib.SMTP('smtp.gmail.com', 587)
    server.starttls()
    server.login(fromaddr, "1PoMmEs1")
    text = msg.as_string()
    server.sendmail(fromaddr, toaddr, text)
    server.quit()

def sellskin():
    mousePos(dic['sell'])
    leftClick()
    time.sleep(0.2)
    mousePos((dic['sell'][0] - 1, dic['sell'][1]))
    leftClick()
    time.sleep(0.2)
    mousePos((dic['sell'][0] - 7, dic['sell'][1]))
    leftClick()

def play():
    lostStreak = 0
    profit = 0
    toBet = masterBet
    setAuto()
    while (isRoundDone() == False):
        time.sleep(.1)
    time.sleep(4.5)
    setBet(masterBet)
    while (lostStreak < NBROUND):
        setAuto()
        while (isRoundDone() == False):
            time.sleep(.1)
        if (isRoundWon()):
            if lostStreak != 0:
                with open("wtflog.txt", "a") as log_file:
                    log_file.write(str(lostStreak) + '\n')
            profit = round(profit + (toBet * 0.01) + (masterBet * 0.5), 5)
            toBet = masterBet
            lostStreak = 0
            print("You won this round! Your profit for this session is: ", str(profit))
        else:
            toBet = round(toBet * 3, 3)
            lostStreak = lostStreak + 1
            print("You lost this round :( Losing streak of: ", lostStreak)
        if (lostStreak < NBROUND):
            print("Next bet is:\t", str(toBet))
            sellskin()
            time.sleep(4.5)
            setBet(toBet)
    sendEmail('Bad', 'You lost all your money :\'(', "vincent.rodier.v@gmail.com")
    sendEmail('Bad', 'You lost all your money :\'(', "etienne.asselin.polymtl@gmail.com")

def setUpValues():
    global dic
    global x_padMainWindow
    global y_padMainWindow
    dic = pickle.load( open( "values.pkl", "rb" ) )
    x_padMainWindow = dic['x_padMainWindow']
    y_padMainWindow = dic['y_padMainWindow']

def main():
    if (os.path.isfile("values.pkl")):
        setUpValues()
        play()
    else:
        print("You must do the setup script first!")

if __name__ == '__main__':
    main()