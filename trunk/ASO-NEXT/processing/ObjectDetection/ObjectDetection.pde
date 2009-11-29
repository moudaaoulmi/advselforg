import processing.video.*;

Capture video;
int numPixels;
int[] backgroundPixels, pixelsRed, pixelsGreen, pixelsBlue;

void setup() {
  size(640, 480);
  video = new Capture(this, 640, 480);
  numPixels = video.width * video.height;
  backgroundPixels = new int[numPixels];
  pixelsRed = new int[numPixels];
  pixelsGreen = new int[numPixels];
  pixelsBlue = new int[numPixels];
  loadPixels();
}

void draw() {
  if (video.available()) {
    video.read();
    video.loadPixels();

    for (int i = 0; i < numPixels; i++) {
      
      color currColor = video.pixels[i];
      color bkgdColor = backgroundPixels[i];

      int currR = (currColor >> 16) & 0xFF;
      int currG = (currColor >> 8) & 0xFF;
      int currB = currColor & 0xFF;

      int bkgdR = (bkgdColor >> 16) & 0xFF;
      int bkgdG = (bkgdColor >> 8) & 0xFF;
      int bkgdB = bkgdColor & 0xFF;

      int diffR = abs(currR - bkgdR);
      int diffG = abs(currG - bkgdG);
      int diffB = abs(currB - bkgdB);

      pixelsRed[i] = 0xFF000000 | (diffR << 16) | (diffR << 8) | diffR;
      pixelsGreen[i] = 0xFF000000 | (diffG << 16) | (diffG << 8) | diffG;
      pixelsBlue[i] = 0xFF000000 | (diffB << 16) | (diffB << 8) | diffB;
      
      pixels[i] = 0xFF000000 | (diffR << 16) | (diffG << 8) | diffB;
    }
    updatePixels();
  }
}

void keyPressed() {
  video.loadPixels();
  arraycopy(video.pixels, backgroundPixels);
}
