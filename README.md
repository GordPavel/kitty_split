# kitty_split
Program calculates the most efficient way to send money to each other after your common party.
It consumes the csv file, where all your purchases are described, who paid for each purchase and for whom.
First line of csv is a header, columns are `payer name`,`purchase name`, 
and amounts, who consumed this purchase. Here is an [input example](examples/input.csv).
Program outputs the csv table, where in rows the sources of transactions are listed, in columns – the destinations participants.
Here is [output example](examples/output.csv). 

You can pass paths for input and output files with `-i` (`--input`) and `-o` (`--output`) relatively
or work with stdin and stdout without specifying paths in flags. 