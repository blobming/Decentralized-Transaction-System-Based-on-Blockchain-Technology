import pymysql
import os

db = pymysql.connect("192.168.1.160", "root", "Supinf0!", "Cloud")
#db = pymysql.connect("criszz.top", "root", "Supinf0!", "Cloud")
cursor = db.cursor()


class User:
    # flask-login必须的属性
    is_authenticated = True
    is_active = True
    is_anonymous = False

    def __init__(self, username):
        # 获取userid作为参数，从数据库中获取信息后填入对应的属性中
        self.username = username
        sql = 'SELECT * FROM User WHERE username = \'' + username + "'"
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                self.username = row[0]
                self.password = row[1]
                self.quota = row[2]
        except Exception as e:
            print(e)

    @staticmethod
    def authenticate(username, password):  # 静态函数，获取用户名密码作为参数，返回信息正确与否 --> 待实现
        sql = "SELECT password FROM User WHERE username= '" + username + "'"
        try:
            os.system("echo 'sql'")
            os.system("echo +'"+sql+"'")
            cursor.execute(sql)
            results = cursor.fetchall()
            print("------authenticate-------")
            print(results)
            for user in results:
                if user[0] == password:
                    os.system("echo 'authenticate pass'")
                    return True
        except Exception as e:
            print(e)
            return False
        return False

    @staticmethod
    def get(username):  # 静态函数，获取userid作为参数，返回对应的user对象 --> OK
        return User(username)

    def get_id(this):  # flask-login必须的方法 --> OK
        return this.username

    @staticmethod
    def newUser(name, pwd, quota):
        # return true if success
        if User.checkUsername(name):
            sql = "INSERT INTO User(`username`, `password`,`quota`) VALUES('%s','%s','%s')" % (name, pwd, quota)
            try:
                cursor.execute(sql)
                db.commit()
            except Exception as e:
                print(e)
            return True
        else:
            return False

    @staticmethod
    def updateUser(password, username):
        sql = "UPDATE `User` SET password = '" + password + "' WHERE `User`.`username` ='" + username + "'"
        try:
            cursor.execute(sql)
            db.commit()
        except Exception as e:
            print(e)

    @staticmethod
    def updateQuota(username, quota):
        sql = "UPDATE User SET quota = " + str(quota) + " WHERE username='" + username + "'"
        try:
            cursor.execute(sql)
            db.commit()
        except Exception as e:
            print(e)

    @staticmethod
    def checkUsername(nameToCheck):
        # to check if name exist in db, return false if exist
        sql = "SELECT username FROM User"
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for name in results:
                if nameToCheck == name[0]:
                    print("false")
                    return False
        except Exception as e:
            print(e)
        return True

    @staticmethod
    def calRemainQuota(username):
        sql = "SELECT quota, SUM(size) FROM File f,UserFileDir u,User WHERE f.md5 = u.fileid AND u.username='" + username + "' AND u.fileid is not null AND u.username = User.username"
        try:
            cursor.execute(sql)
            result = cursor.fetchone()
            print("------quota-------")
            print(result)
            return result[0] - (result[1] if result[1] is not None else 0)
        except Exception as e:
            print(e)
            return -1
