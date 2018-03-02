import sys
import os

result = ""
for i in range(0, 9999):
	millier = i / 1000
	centaine = (i / 100) % 10
	dizaine = (i / 10) % 10
	unite = i % 10
	string = "python trans1.py " + str(millier) + str(centaine) + str(dizaine) + str(unite) + " >> resultCodage1.txt"
	os.system(string)
	with open("resultCodage1.txt", "a") as file:
		file.write('\n')
print ("Done")
