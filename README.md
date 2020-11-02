# BCypher
Encodes and decodes cyphers:
   * Vigenere
   * Atbash
   * Caesar
   * Symbol replacement

Offers possibility to save cypher keys to be used another time in .txt format

Ignores Upper/Lower case letters

using list of english words with number of occurences on the internet: http://norvig.com/ngrams/count_1w.txt





### Symbol replacement Cypher
  * each letter is replaced with some other
  * this can replace Atbash and Caesar but not Vigenere
  * needs lot of words when decoding (like circa 10, the longer the better)

#### How to use Symbol replacement Decypher
  1. First find text which you know is english and contains symbols which each stands for different letter in normal latin alfabet
  2. Write down all the symbols and assign each of them some letter of latin alfabet (doesn't matter which as long as they differ from each other)
  3. Now it's better to make .txt file and write the cypher using the letters of latin alfabet replacing the symbols (you'll have something like "svool gzebpp, we p hu cfg.....")
  4. Now launch the Decypher and select Symbol cypher
  5. Copy text from .txt file to left window of the program
  6. Hit that "Try to decode" button and wait
  7. After a while right output should be present 
  8. Congratulations! You've decoded the cypher.
  9. In two center columns you can see which letter is translated to the other (from left to right respectively)
  10. You can alter the remaining characters from the center columns. (There could be some mistakes)
  11. You can save the key using File -> Save
  
  ##### Note:
  If the program gets stuck at step 6, terminate it and use other set of words for the decoding process. Some word is probably not         present in the dictionary.
  
