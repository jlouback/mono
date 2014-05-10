function retval = ultimateWidth(x)

  #Calculating Ultimate Width - See section 3.1 forumula (6)
  retval = 2*var(x)/mean(x);
  
endfunction