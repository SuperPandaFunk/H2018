e = 311
n = 288419
tmp = {0:0}
for i in range(1,25):
    tmp.update({(i**e)%n:i})
print(chr(tmp[0] + ord('A')), chr(tmp[81902] + ord('A')), chr(tmp[81902] + ord('A')), chr(tmp[71381] + ord('A')), chr(tmp[139280] + ord('A')))