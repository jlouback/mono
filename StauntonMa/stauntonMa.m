function stauntonMa(filepath, flag)

  printf(filepath);
  printf("\n");

  csvFilname = "csv_data.csv";
  
  # Obtain contour signature - See section 2.1
  Cr = lesionRadii(filepath);

  #run multilevel discrete wavelet transform to level 9 - See section 5.1
  [coefficients,info] = fwt(Cr,'db3', 9);


  #organizing coefficients into sub-bands 
  subbands = wavpack2cell(coefficients,info.Lc);


  #obtaining the 13 features used in algorithm
  #Obs: the vector 'subbands' is organized top-down: A9, D9, D8, D7, ... D1

  #Statistical measures - see section 3.1
  
    #Calculating Mean Energy for subbands 6-8 inclusive
    meanEnergy_8 = num2str(mean(subbands{3}));
    meanEnergy_7 = num2str(mean(subbands{4}));
    meanEnergy_6 = num2str(mean(subbands{5}));
    
    #Calculating Entropy of wavelet energy for subbands 6-9 inclusive
    energyEntropy_9 = num2str(energyEntropy(subbands{2}));
    energyEntropy_8 = num2str(energyEntropy(subbands{3}));
    energyEntropy_7 = num2str(energyEntropy(subbands{4}));
    energyEntropy_6 = num2str(energyEntropy(subbands{5}));
    
    
    #Calculating Ultimate Width for subbands 6, 7 and 9
    ultimateWidth_9 = num2str(ultimateWidth(subbands{2})); 
    ultimateWidth_7 = num2str(ultimateWidth(subbands{4})); 
    ultimateWidth_6 = num2str(ultimateWidth(subbands{5})); 



  #Geometric based irregularity measures - see section 3.2

  #structural component reconstruction for subband 6
  structuralComponent_6 = structuralComponent(Cr, info.Lc, 6);
    
    #Calculating Radial Deviation for subband 6
    radialDeviation_6 = num2str(std(structuralComponent_6));
  
  #structural component reconstruction for subband 7
  structuralComponent_7 = structuralComponent(Cr, info.Lc, 7);
    
    #Calculating Contour Roughness for subband 7
    contourRoughness_7 = num2str(contourRoughness(structuralComponent_7));
    
  
  #Name of file is used as ID
  nameIndex = index(filepath, "/", "last");
  nameIndex = nameIndex + 1;
  filename = substr (filepath, nameIndex);
  #Put data in vector
  data = {filename,meanEnergy_6,meanEnergy_7,meanEnergy_8,energyEntropy_6,energyEntropy_7,energyEntropy_8,energyEntropy_9,ultimateWidth_6,ultimateWidth_7,ultimateWidth_9,radialDeviation_6,contourRoughness_7,flag};
  #Add data to csv file:
  fid=fopen(csvFilname,'a');
  [rows,cols]=size(data)
  for i=1:rows
        fprintf(fid,'%s,',data{i,1:end-1})
        fprintf(fid,'%s\n',data{i,end})
  end
  fclose(fid);
  
    
endfunction