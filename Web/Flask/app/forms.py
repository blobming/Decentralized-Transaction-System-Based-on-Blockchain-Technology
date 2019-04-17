from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, BooleanField, SubmitField
from wtforms.validators import DataRequired


class LoginForm(FlaskForm):  # wtf form for login
    username = StringField("Username", validators=[DataRequired("Please input username")])
    password = PasswordField("Password", validators=[DataRequired("Please password")])
    rememberMe = BooleanField('Remember Me')
    submit = SubmitField("Login")


class RegisterForm(FlaskForm):
    username = StringField("Username", validators=[DataRequired("Please input username")])
    password = PasswordField("Password", validators=[DataRequired("Please password")])
    submit = SubmitField("Register")
