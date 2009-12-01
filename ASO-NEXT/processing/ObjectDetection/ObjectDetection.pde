import processing.video.*;
import java.util.Iterator;
import java.util.ArrayList;

static final int NORMAL = -1, RED = 0, GREEN = 1, BLUE = 2, BACKGROUND = 3;

Capture video;
int numPixels;
int[][] colors;
boolean[][] bools;
int displayMode = NORMAL;
BlobDetection detector;
  
void setup() {
  size(640, 480);
  video = new Capture(this, 640, 480, "macam #0: Logitech QuickCam Messenger", 1);
  numPixels = video.width * video.height;
  colors = new int[4][numPixels];
  bools = new boolean[2][numPixels];
  detector = new BlobDetection();
  loadPixels();
}

void draw() {
  if (video.available()) {
    video.read();
    video.loadPixels();

    for (int i = 0; i < numPixels; i++) {
      
      color currColor = video.pixels[i];
      color bkgdColor = colors[BACKGROUND][i];

      int currR = (currColor >> 16) & 0xFF;
      int currG = (currColor >> 8) & 0xFF;
      int currB = currColor & 0xFF;

      int bkgdR = (bkgdColor >> 16) & 0xFF;
      int bkgdG = (bkgdColor >> 8) & 0xFF;
      int bkgdB = bkgdColor & 0xFF;

      int diffR = abs(bkgdR - currR);
      int diffG = abs(bkgdG - currG);
      int diffB = abs(bkgdB - currB);
      
      colors[RED][i] = 0xFF000000 | (currR << 16) | (currR << 8) | currR;
      colors[GREEN][i] = 0xFF000000 | (currG << 16) | (currG << 8) | currG;
      colors[BLUE][i] = 0xFF000000 | (currB << 16) | (currB << 8) | currB;
      
      bools[RED][i] = diffR > 30 && brightness(colors[RED][i]) > brightness(colors[GREEN][i]) + brightness(colors[BLUE][i]) - 10;
      bools[GREEN][i] = diffG > 30 && brightness(colors[RED][i]) > 1.35 * brightness(colors[BLUE][i])
                     && brightness(colors[GREEN][i]) > 1.35 * brightness(colors[BLUE][i])
                     && Math.abs(brightness(colors[GREEN][i]) - brightness(colors[RED][i])) < 12;
      
      switch (displayMode) {
        case NORMAL:
          pixels[i] = currColor;
          break;
        case BACKGROUND:
          pixels[i] = 0xFF000000 | (diffR << 16) | (diffG << 8) | diffB;
          break;
        case RED:
          pixels[i] = bools[RED][i] ? 0xFFFFFFFF : 0xFF000000;
          break;
        case GREEN:
          pixels[i] = bools[GREEN][i] ? 0xFFFFFFFF : 0xFF000000;
          break;
      }
    }
    updatePixels();
  }
}

void drawBlob(int x, int y) {
    fill(255, 0, 0, 128);
    ellipse(x, y, 15, 15);
}

void keyPressed() {
  switch (key) {
  case 'b':
    displayMode = BACKGROUND;
    video.loadPixels();
    arraycopy(video.pixels, colors[BACKGROUND]);
    break;
  case 'n':
    displayMode = NORMAL;
    break;
  case 'r': 
    displayMode = RED;
    break;
  case 'g': 
    displayMode = GREEN;
    break;
  case 'd':
    ArrayList clusters = detector.process(bools[RED]);
    for (int i = 0; i < clusters.size(); i++) {
      PixelCluster p = (PixelCluster) clusters.get(i);
      drawBlob(p.center().getX(), p.center().getY());
    }
    break;
  }
}
