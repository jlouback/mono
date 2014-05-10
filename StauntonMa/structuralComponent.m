function retval = structuralComponent(Cr, Lc, subbandNumber)

  #Find limit of requested subband within coefficients
  limit = sum(Lc(1:(subbandNumber-1)));
  limit = limit+1;
  
  #removing coefficients beyond subband
  coefficients = fwt(Cr,'db3', 9);
  coefficients(1, limit:length(coefficients)) = zeros;
  
  #reconstruction through inverse wavelet transform
  structuralComponent = ifwt(coefficients,'db3',9,length(Cr));
  
  retval = structuralComponent;
  
endfunction