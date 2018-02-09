#!/usr/bin/env python
import sys
from Crypto import Random
from Crypto.Cipher import DES


def main(argv=None):
    """
    Ce script implemente le codeur 1 suivi du chiffrement DES
    Usage:
        trans1 NIP

    Le NIP doit avoir exactement 4 chiffres
    """
    if argv is None:
        argv = sys.argv

    if len(argv) != 2:
        print 'Erreur de syntaxe'
        print main.__doc__
        return 1

    pin = argv[1]
    if len(pin) != 4 or not(pin.isdigit()):
        print "Le NIP doit avoir exactement 4 chiffres"
        return 2

    salt = Random.new().read(6)
    pin_bin = bin(int(pin))[2:].zfill(14)
    pin_bin += str(pin_bin[:7].count('1') % 2)
    pin_bin += str(pin_bin[7:].count('1') % 2)

    code = chr(int(pin_bin[:8], 2)) \
        + chr(int(pin_bin[8:], 2)) \
        + salt

    # Cle generee par Random.new().read(DES.key_size)
    key = '\xe8\x8e\x0e\x16-\x88\xf6\x10'

    # IV genere par Random.new().read(DES.block_size)
    iv = '\x9a=\xa7#+\x85\xf3\xab'

    cipher = DES.new(key, DES.MODE_CBC, iv)
    ciphertext = cipher.encrypt(code)
    sys.stdout.write(ciphertext)

if __name__ == '__main__':
    main()
