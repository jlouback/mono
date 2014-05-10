function runStauntonMa(directory, flag)
  
  #directory contains the path to the images to be analyzed
  #flag indicates if the image in the directory are of melanoma('true') or nevi('false');
  #Get list of images to be processed
  files = readdir(directory);
  
  #Run the StauntonMa algorithm on every file in directory specified
  for i=4:6
    filepath = strcat(directory,'/',files{i});
    stauntonMa(filepath, flag);
  endfor

endfunction