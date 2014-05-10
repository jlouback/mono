function retval = energyEntropy(D)

  #Calculating total energy of wavelet subband - See section 2.2.1 forumula (2)
  E = sum(D.^2);

  W = 0;
  #Calculating wavelet energy entropy W - See section 3.1 forumula (5)
  for i = 1:length(D)
    Ei = D(i)^2;
    Pi = Ei/E;
    W = W + (Pi*log(Pi));
  endfor
  W = -W;

  retval = W;
endfunction