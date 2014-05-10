function retval = contourRoughness(x)
  #For formula, see section 3.2
  size = length(x);
  sum = 0;
  for n = 1:size-1
    sum = sum + abs(x(n) - x(n+1));
  end
  sum = sum + abs(x(size) - x(1));
  retval = sum/size;
endfunction