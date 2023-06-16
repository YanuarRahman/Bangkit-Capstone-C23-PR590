from fastapi import FastAPI, File, UploadFile
import uvicorn
import numpy as np
from io import BytesIO
from PIL import Image
import tensorflow as tf
from fastapi.middleware.cors import CORSMiddleware
import uuid
import os
from random import randint

IMAGEDIR = "../images/"

app = FastAPI()

origins = [
    "http://localhost",
    "http://localhost:3000",
]

app.add_middleware(
    CORSMiddleware,
    allow_origins = origins,
    allow_credentials = True,
    allow_methods=['*'],
    allow_headers=['*'],
)

CLASS_NAMES = ['Courteney_Cox', 'David_Schwimmer', 'Jennifer_Aniston', 'Jim_Parsons', 'Johnny_Galeck', 'Kunal_Nayya', 'Lisa_Kudrow', 'Matt_LeBlanc', 'Matthew_Perry', 'Pankaj_Tripathi', 'ROHIT_SHARMA', 'Sachin_Tendulka', 'Simon_Helberg','aishwarya_rai','angelina_jolie', 'arnold_schwarzenegger', 'bhuvan_bam', 'brad_pitt', 'dhoni', 'hardik_pandya', 'manoj_bajpayee', 'messi', 'mohamed_ali', 'pewdiepie', 'random_person', 'ronaldo', 'scarlett_johansson', 'suresh_raina', 'sylvester_stallone', 'virat_kohli']
MODEL = tf.keras.models.load_model("../model/faceRecog.h5", compile=False)
@app.get("/ping")
async def ping():
    return "Hello, World!"


def read_file_as_image(data) -> np.ndarray :
    image = np.array(Image.open(BytesIO(data)))
    return image

@app.post("/recognition")
async def predict(file: UploadFile = File(...)):  # 
    file.filename = f"{uuid.uuid4()}.jpg"
    contents = await file.read()
 
    # #save the file
    with open(f"{IMAGEDIR}{file.filename}", "wb") as f:
        f.write(contents)
    # #path
    # files = os.listdir(IMAGEDIR)
    # random_index = randint(0, len(files) - 1)
 
    # path = f"{IMAGEDIR}{files[random_index]}"
    file_path = f"{IMAGEDIR}{file.filename}"
    # print(path)
    #image =  read_file_as_image (await file.read())
    img = tf.keras.utils.load_img(file_path, target_size=(224,224,3))

    x = tf.keras.utils.array_to_img(img)
    x = np.expand_dims(x, axis=0)
    x = x / 255.0
    # images = np.vstack([x])
    pred = MODEL.predict(x)
    predicted_class = CLASS_NAMES[np.argmax(pred[0])]
    confidence = np.max(pred[0])

    os.remove(file_path)

    return {
        'class' : predicted_class,
        'confidence' : float(confidence)
    }


if __name__ == "__main__":
    uvicorn.run(app, host='localhost', port=8000)

