import time
import os
import win32api, win32con
from PIL import ImageGrab
from PIL import ImageOps
from numpy import *
import pickle

clear = lambda: os.system('cls')

x_padMainWindow = 0
y_padMainWindow = 0

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

def keyPress(arg):
    win32api.keybd_event(VK_CODE[arg], 0,0,0)
    time.sleep(.1)

def leftClick():
    win32api.mouse_event(win32con.MOUSEEVENTF_LEFTDOWN, 0, 0)
    time.sleep(.1)
    win32api.mouse_event(win32con.MOUSEEVENTF_LEFTUP, 0, 0)

def mousePos(cord):
    win32api.SetCursorPos((x_padMainWindow + cord[0], y_padMainWindow + cord[1]))

def screenGrabMainWindow(box):
    box = (x_padMainWindow + box[0], y_padMainWindow + box[1], x_padMainWindow + box[2], y_padMainWindow + box[3])
    im = ImageGrab.grab(box)
    im.save(os.getcwd() + "\\full_snap__" + str(int(time.time())) + ".png", 'PNG')

def get_absolute_cords():
    x,y = win32api.GetCursorPos()
    x = x
    y = y
    return [x, y]

def get_cords():
    x,y = win32api.GetCursorPos()
    x = x - x_padMainWindow
    y = y - y_padMainWindow
    return [x, y]

def get_box_value(box):
    box = (x_padMainWindow + box[0], y_padMainWindow + box[1], x_padMainWindow + box[2], y_padMainWindow + box[3])
    im = ImageOps.grayscale(ImageGrab.grab(box))
    a = array(im.getcolors())
    a = a.sum()
    return a

def main():
    global x_padMainWindow
    global y_padMainWindow
    dic = {}
    clear()
    print("Welcome to setup your bot!")
    input()
    clear()
    print("In this script we will setup your bot for your own screen size and resolution!")
    input()
    clear()
    print("Please make sure that wtfskins is open on your screen and that the website is exactly the way it will be when you run the bot!")
    input()
    input("Press enter when this step is done.")
    clear()

    ### First coordonates, main graph window ###
    print("Awesome! Now the first thing to do is to set your cursor in the top left hand corner of the crash graph. (For visual aid, look at the picture \"Crash_top_left\" in the folder \"Help\")")
    input("\nPress enter when your cursor is correctly placed.")
    x_padMainWindow, y_padMainWindow = get_absolute_cords()
    dic['x_padMainWindow'] = x_padMainWindow
    dic['y_padMainWindow'] = y_padMainWindow
    clear()

    input("Now place your cursor at the bottom right corner of the graph and press enter (Again for reference, please see \"Crash_bottom_right\")")
    bottomRight = get_cords()
    clear()
    box = [1, 1, bottomRight[0], bottomRight[1]]
    screenGrabMainWindow(box)
    print("Good, the first coordonates are set! Please open the folder Comparisson and make sure there is a picture of the crash graph named \"Main_window\" and that it looks like the picture \"Ref_main_window\"")
    input("If everything is ok, press enter to continue")
    clear()

    ### Setting the auto box ###
    print("Now we\'re going to set up your bot to recognize the Auto box.")
    input("Place your cursor at the top left hand corner of the auto option (Ref: \"Auto_top_left\") and press enter")
    tmp = get_cords()
    dic['auto_top_left'] = tmp
    input("Put your cursor at the bottom right corner of the auto option and press enter. Make sure to include the little switch in the box we are making! (Ref: \"Auto_bottom_right\")")
    tmp = get_cords()
    dic['auto_bottom_right'] = tmp
    clear()
    box[0] = dic['auto_top_left'][0]
    box[1] = dic['auto_top_left'][1]
    box[2] = dic['auto_bottom_right'][0]
    box[3] = dic['auto_bottom_right'][1]
    screenGrabMainWindow(box)
    print("Go and make sure that a new picture appeared in the Comparisson folder. Verify that \"Auto_box\" is similar to \"Ref_auto_box\".")
    input("Press enter to continue")
    clear()

    ### Setting the auto to ON value ###
    input("Now set the auto option on wtfskins to ON and press enter")
    box[0] = dic['auto_top_left'][0]
    box[1] = dic['auto_top_left'][1]
    box[2] = dic['auto_bottom_right'][0]
    box[3] = dic['auto_bottom_right'][1]
    dic["auto_on_value"] = get_box_value(box)

    clear()
    input("Put your cursor on top of the auto option just like if you wanted to click on it, but don\'t click,  press enter.")
    dic['auto_click'] = get_cords()
    clear()

    ### First test of auto ###
    input("The auto option is all set, turn off the auto option on wtfskins and press enter")
    print(str(get_box_value(box)), '\t', str(dic['auto_on_value']))
    if (get_box_value(box) != dic['auto_on_value']):
        mousePos(dic['auto_click'])
        leftClick()
    clear()
    input("The auto option should now have been turn on without you doing anything. Amazing! If nothing happend ctrl+c and do it again slowler, you likely skipped something.")
    clear()

    ### Setting the muliplier ###
    print("Next: setting the box for the multiplier.")
    input("\nFirst things first, write the number \"1.51\" in the multiplier box and press enter.")
    clear()
    input("Place your cursor at the top left corner of the multiplier box. Press enter. (Ref: \"Multiplier_top_left\")")
    dic['multiplier_top_left'] = get_cords()
    box[0] = dic['multiplier_top_left'][0]
    box[1] = dic['multiplier_top_left'][1]
    input('\nPlace your cursor at the bottom right corner of the multiplier box and press enter.')
    dic['multiplier_bottom_right'] = get_cords()
    box[2] = dic['multiplier_bottom_right'][0]
    box[3] = dic['multiplier_bottom_right'][1]
    screenGrabMainWindow(box)
    dic['multiplier_value'] = get_box_value(box)
    clear()
    input('Make sure the picture \"Multiplier_box\" looks like the picture \"Ref_multiplier_box\" and press enter')
    input("Place your cursor on top of the first number \"1\"in the multiplier box and press enter")
    dic['multiplier_click'] = get_cords()
    clear()
    input("Lastly with the multiplier box, erase everything inside the multiplier box and press enter.")
    dic['multiplier_empty_value'] = get_box_value(box)
    input("Let\'s test the new number box shall we? You can leave the multiplier box empty or write something in it and press enter.")
    clear()

    ### Testing the multiplier box ###
    while (get_box_value(box) != dic['multiplier_empty_value']):
        mousePos(dic['multiplier_click'])
        leftClick()
        keyPress('backspace')
    mousePos(dic['multiplier_click'])
    leftClick()
    keyPress('1')
    keyPress('.')
    keyPress('5')
    keyPress('1')

    input("If \"1.51\" is not written in the multiplier box, something whent wrong. Enter to continue")
    clear()

    ### Verifying the round state ###
    input("Now we\'re going to make the bot able to recognize if the round is over")
    input('Place your cursor right above and a little bit to the left of the \"M\"of \"Max Bet: 5000\" in the crash window. Press enter.(Ref: \"Max_bet_top_left\")')
    dic['maxBet_top_left'] = get_cords()
    box[0] = dic['maxBet_top_left'][0]
    box[1] = dic['maxBet_top_left'][1]
    clear()
    input('Place your cursor at the bottom right of \"Max Bet: 5000\" in the crash window. PRESS ENTER WHEN THE TEXT IS GRAY.(Ref: \"Max_bet_bottom_right\")')
    dic['maxBet_bottom_right'] = get_cords()
    box[2] = dic['maxBet_bottom_right'][0]
    box[3] = dic['maxBet_bottom_right'][1]
    dic['gray_max_bet'] = get_box_value(box)
    screenGrabMainWindow(box)
    clear()
    input("Make sure the the picture of the max bet is ok and that the text is gray. Press enter.")

    ### Making sure the bot knows if the round is over ###
    clear()
    input("Let\'s see if our bot sees when the round is over. Press enter during the countdown before a crash.")
    while (get_box_value(box) == dic['gray_max_bet']):
        time.sleep(.1)
    input("The round is over! Press enter to continue.")
    clear()

    ### Setting the bot to recognize the win condition ###
    print("You\'re making good progress! Not much more to do. We are now going to make the bot know if we won of lose the round.\n")
    input('Place your cursor on the top left of \"Nothing to cashout\" in the bidding box. Press enter. (Ref: \"NothingCashOut_top_left\")')
    dic['nothing_top_left'] = get_cords()
    box[0] = dic['nothing_top_left'][0]
    box[1] = dic['nothing_top_left'][1]
    clear()
    input('Place your cursor on the bottom right of \"Nothing to cashout\" in the bidding box. Press enter. (Ref: \"NothingCashOut_bottom_right\")')
    dic['nothing_bottom_right'] = get_cords()
    box[2] = dic['nothing_bottom_right'][0]
    box[3] = dic['nothing_bottom_right'][1]
    dic['nothing_lost_value'] = get_box_value(box)
    screenGrabMainWindow(box)
    clear()
    input("Make sure you see the new picture with \"Nothing to cashout\". Press enter.")
    clear()

    ### Bidding ###
    input("Making a bet! Empty the wager box and place your cursor top left of the wager box. Press enter. (Ref: \"Wager_top_left\")")
    dic['wager_top_left'] = get_cords()
    box[0] = dic['wager_top_left'][0]
    box[1] = dic['wager_top_left'][1]
    clear()
    input("Place your cursor bottom right of wager input box. Press enter. (Ref: \"Wager_bottom_right\")")
    dic['wager_bottom_right'] = get_cords()
    box[2] = dic['wager_bottom_right'][0]
    box[3] = dic['wager_bottom_right'][1]
    screenGrabMainWindow(box)
    dic['wager_empty'] = get_box_value(box)
    clear()
    input("Make sure the picture with the wager box is ok. Press enter")
    clear()
    input("Place your cursor on top of the \"0\" in the wager box")
    dic['wager_click'] = get_cords()

    ### Making sure bidding is ok ###
    clear()
    input("Press enter to make sure bidding is ok. The bet is 0.01. (Don\'t worry it won\'t enter the round)")
    while (get_box_value(box) != dic['wager_empty']):
        mousePos(dic['wager_click'])
        leftClick()
        keyPress('backspace')
    mousePos(dic['wager_click'])
    leftClick()
    bet = 0.01
    for letter in list(str(bet)):
        keyPress(letter)
    clear()
    input("If everything is still of, press enter.")
    clear()

    ### Entering the round ###
    input("Place your cursor on top of the \"Enter\" for the round and press enter.")
    clear()
    dic['enter'] = get_cords()

    ### Selling skins ###
    print("Last but not least we are going to sell the skins you make while playing!\n")
    input("It's ok if you don\'t have a skin right now. Place you\'re cursor somewhere where clicking is safe and come back to this setup when you get a skin. Press enter.")
    dic['sell'] = get_cords()

    with open('values.pkl', 'wb') as file:
        pickle.dump(dic, file)
    
    clear()
    print("That\'s it, you\'re done and ready to start up the bot the see if you did a good job! Press enter to finish the setup.")



if __name__ == '__main__':
    main()