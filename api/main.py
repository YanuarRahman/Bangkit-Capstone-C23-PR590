from fastapi import FastAPI, Request
import uvicorn
import os
import tensorflow as tf
from dotenv import load_dotenv
from api.process import get_menu_data, parse_user_data, preprocess, get_recommended_foods

load_dotenv()

app = FastAPI()

CONN = os.getenv("MONGODB_URL")
DB = os.getenv("DATABASE")
MENU_COLLECTION = os.getenv("PRODUCT_COLLECTION")


@app.post("/predict")
async def predict(request: Request):
    try:
        user = await request.json()

        menu_docs, menu_df = get_menu_data(CONN, DB, MENU_COLLECTION)

        user_df = parse_user_data(user)

        menu_vecs, user_vecs = preprocess(menu_df, user_df)

        model = tf.keras.models.load_model('api/modelv2')

        predictions = model.predict([user_vecs, menu_vecs])

        rec = get_recommended_foods(predictions, menu_docs)

        return rec

    except Exception as e:
        # Handle the exception
        return {"error", str(e)}