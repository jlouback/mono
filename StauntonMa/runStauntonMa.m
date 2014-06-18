function runStauntonMa(directory, flag)
  
  #directory contains the path to the images to be analyzed
  #flag indicates if the image in the directory are of melanoma('true') or nevi('false');
  #Get list of images to be processed
  files = readdir(directory);
  
  [row, column] = size(files);
  disp(row);
  disp(column);
  
  #Run the StauntonMa algorithm on every file in directory specified
  #starts on 3 to skip Mac OSX config files
  for i=3:row
    filepath = strcat(directory,'/',files{i});
    stauntonMa(filepath, flag);
  endfor

endfunction