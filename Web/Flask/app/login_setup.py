from flask_login import LoginManager
from .models.user import User

loginManager = LoginManager()

@loginManager.user_loader
def load_user(userid):  # set up for flask-login
    return User.get(userid)

def login_init(app):
	app.config['SECRET_KEY']='xxx'  # set up csfr secret key
	loginManager.init_app(app)
