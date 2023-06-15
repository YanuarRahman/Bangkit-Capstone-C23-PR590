from fastapi import FastAPI, Request
import uvicorn
import os
import tensorflow as tf
from dotenv import load_dotenv
from api.process import get_menu_data, parse_user_data, preprocess, get_recommended_foods

load_dotenv()

app = FastAPI()


@app.post("/predict")
async def predict(request: Request):
    try:
        user = await request.json()

        menu_df = get_menu_data(os.getenv("MONGODB_URL"), 'recomenu', 'Food')

        user_id, user_df = parse_user_data(user)

        menu_ids, menu_vecs, user_vecs = preprocess(menu_df, user_df)

        model = tf.keras.models.load_model('api/modelv2')

        predictions = model.predict([user_vecs, menu_vecs])

        rec = get_recommended_foods(predictions, menu_ids)

        return {'uid': user_id, 'recommendation': rec}

    except Exception as e:
        # Handle the exception
        return {"error", str(e)}


