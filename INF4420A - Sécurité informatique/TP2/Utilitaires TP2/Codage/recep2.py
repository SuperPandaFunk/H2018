#!/usr/bin/env python
import sys
import getopt
from Crypto.Cipher import DES
from Crypto.Util.number import bytes_to_long
from time import time


def main(argv=None):
    """
    Ce script implemente le dechiffrement DES suivi du decodeur 2
    Options :
        -d, --delay     delai autorise en secondes (0 par defaut)
    """
    if argv is None:
        argv = sys.argv
    try:
        opts, args = getopt.getopt(argv[1:], "hd:",
                                   ["help", "delay="])
    except getopt.error, msg:
        print msg
        print "for help use --help"
        return 2
    # process options
    for o, a in opts:
        if o in ("-h", "--help"):
            print main.__doc__
            return 0

        elif o in ("-d", "--delay"):
            if a.isdigit():
                delay = int(a)
            else:
                print "Le delai doit etre numerique"
                print main.__doc__

    try:
        delay
    except:
        delay = 0

    if args:
        print main.__doc__
        return 1

    ciphertext = sys.stdin.read(8)
    if len(ciphertext) != 8:
        print "Le texte chiffre doit avoir exactement 64 bits"
        return 2

    # Cle generee par Random.new().read(DES.key_size)
    key = '\xe8\x8e\x0e\x16-\x88\xf6\x10'

    # IV genere par Random.new().read(DES.block_size)
    iv = '\x9a=\xa7#+\x85\xf3\xab'

    cipher = DES.new(key, DES.MODE_CBC, iv)
    code = cipher.decrypt(ciphertext)
    timestamp = bytes_to_long(code[-4:])

    if (int(time()) - timestamp) > delay:
        print 'Delai de transmission suspect, operation annulee'
        return 3

    pin_bin = bin(ord(code[0]))[2:].zfill(8) \
        + bin(ord(code[1]))[2:].zfill(8)
    parity_chk = str(pin_bin[:7].count('1') % 2)
    parity_chk += str(pin_bin[7:14].count('1') % 2)

    if (pin_bin[-2:] != parity_chk):
        print 'Erreur dans la transmission'
        return 4

    pin = int(pin_bin[:14], 2)

    sys.stdout.write(str(pin))

if __name__ == '__main__':
    main()
