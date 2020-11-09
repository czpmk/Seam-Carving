# Seam-Carving 
Project created as an introduction to Kotlin. 
- created by: Michał Czapiewski
- email: czapiewskimk@gmail.com
- Github: https://github.com/czpmk
The program is an image resizing algorithm implementation, based on the Seam Carving 
algorithm. To find seams the program calculates the energy of pixels and searches the
path of pixels with the lowest sum of energy possible. Keeping in mind a possible 
mobile application the most effort is put into efficiency and versatility. 

---
# Efficiency measure

As a measure of efficiency, each version of the algorithm is tested by recording 
time necessary to remove 150 vertical and 50 horizontal seams of 4 images:
500x300, 600x400, 1920x1200 and 3000x2000 pixels. 

---
# Current state
- Seam finding algorithm - implemented using a dynamic approach. Sums of 
energy is calculated by iterating through an entire matrix every cycle,
which has proven to be much more efficient than queuing the pixels by a minimum sum
of energy.
- Supported formats: .BMP
- Efficiency (150/50 [V/H] seams removal): 
[resolution: time (previous result)]
  * 500 x 300: 3.71s (1m 9s)
  * 600 x 400: 5.45s (2m 28s)
  * 1920 x 1200: 45.10s (13m 52s)
  * 3000 x 2000: 3m 18.00s (not recorded)
- Gaussian blur - implemented to reduce the graininess of the images.
- Conversion to grayscale - image is being converted to grayscale before calculating
the energies.
- Filtering - Kernel class has been implemented to provide quick access to
various methods of image filtering as well as energy calculation. Basic gradient, 
Sobel Filters and Gaussian Blur has been implemented using Kernel class.

---
# In progress
- Conversion to grayscale via Kernel.
- Variable parameters for blur Kernel.
- Efficiency improvement.

---
# Further development plan
- Image scaling implementation, to provide seam carving in a reasonable time.
- Support for different image formats
