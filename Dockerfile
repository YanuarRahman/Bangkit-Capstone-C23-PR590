# Use an official Python runtime as the base image
FROM python:3.11

# Set the working directory in the container
ENV HOME_APP /app
WORKDIR $HOME_APP

# Copy the requirements.txt file to the container
COPY requirements.txt .

# Copy the application code to the container
COPY . ./

# Install the Python dependencies
RUN pip install -r requirements.txt

# Expose the port your application listens on
EXPOSE 8000

# Set the entrypoint command to run your application
CMD exec uvicorn api.main:app --host=0.0.0.0 --port=8000