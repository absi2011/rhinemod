from PIL import Image
import os
for filename in os.listdir("./upscaled"):
    img = Image.open("upscaled/" + filename)
    img_new = img.crop((6, 66, 506, 446))
    img_new.save("cropped/" + filename)
