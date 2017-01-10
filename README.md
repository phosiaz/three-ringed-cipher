# three-ringed-cipher
Simulates a three-ringed Vigenere cipher (polyalphabetic).
Encrypts/Decrypts given files or user input
Creates a random cipher if not provided with one

Details on how the cipher works:
There are three rings, each containing the entire alphabet in a randomized order.
The vigenere cipher finds the character to be encrypted on the inner ring 
of the cipher, and records its index.
It finds the character on the middle ring with the corresponding
index, then finds the index corresponding character on the outer ring and records that index.
We then take this final index and find the corresponding character on the inner
ring, and replace the character in the message.
Then, we rotate the inner ring one space (index increases by 1).  Once the inner ring has rotated one full circle,
rotate the middle ring by one character. Return to rotating the inner ring, and repeat the cycle.
