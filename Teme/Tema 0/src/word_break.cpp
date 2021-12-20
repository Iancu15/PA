// A recursive program to print all possible
// partitions of a given string into dictionary
// words
#include <iostream>
using namespace std;
 
/* A utility function to check whether a word
  is present in dictionary or not.  An array of
  strings is used for dictionary.  Using array
  of strings for dictionary is definitely not
  a good idea. We have used for simplicity of
  the program*/
int dictionaryContains(string &word, string dictionary[], int dict_size)
{
    for (int i = 0; i < dict_size; i++)
        if (dictionary[i].compare(word) == 0)
            return true;
    return false;
}

// Result store the current prefix with spaces
// between words
void wordBreak(string str, int n, string result, string dictionary[], int dict_size)
{
    cout << str << "\n";
    //Process all prefixes one by one
    for (int i=1; i<=n; i++)
    {
        // Extract substring from 0 to i in prefix
        string prefix = str.substr(0, i);
 
        // If dictionary conatins this prefix, then
        // we check for remaining string. Otherwise
        // we ignore this prefix (there is no else for
        // this if) and try next
        if (dictionaryContains(prefix, dictionary, dict_size))
        {
            // If no more elements are there, print it
            if (i == n)
            {
                // Add this element to previous prefix
                result += prefix;
                cout << result << endl;
                return;
            }

            wordBreak(str.substr(i, n-i), n-i,
                                result + prefix + " ", dictionary, dict_size);
        }
    }     
}

//Driver Code
int main(int argc, char* argv[])
{
    string dictionary[] = {"mobile","samsung","sam","sung",
                            "man","mango", "icecream","and",
                            "go","i","love","ice","cream"};

    string str = argv[1];
    wordBreak(str, str.size(), "", dictionary, 13);
    return 0;
}