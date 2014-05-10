function retval = lesionRadii(filename)
  pkg load image;
  img = imread(filename);
  #image must be in grayscale for manipulation
  imgBW = rgb2gray(img);
  
  # Get edge and centroid coordinates - See section 2.1
  edges = edge(imgBW, 'canny', 2, 8);
  [x,y] = find(edges);
  
  #amount of coordinates
  points = size(x, 1);

  #Obtaining the centroid of the lesion.
  xCentroid = min(x) + ((max(x)-min(x))/2);
  yCentroid = min(y) + ((max(y)-min(y))/2);
 
  
  # Model 2D data as 1D: create radii vector using euclidean distance - See section 2.1
  Cr=zeros(1, points);
  for n = 1:points
    Cr(1, n) = sqrt((x(n, 1)-xCentroid)^2+(y(n, 1)-yCentroid)^2);
  end
  retval = Cr;
endfunction