from flask import Flask, render_template, request, render_template_string

app = Flask(__name__)
app.config["SERVER_NAME"] = "localhost:9999"

@app.route('/')
def index():
    user_input = ''
    user_input = request.args.get("q")
    if request.method == 'POST':
        user_input = request.form['user_input']
    if user_input:
        return render_template_string(user_input)
    return render_template('index.html', user_input=user_input)

@app.route('/', subdomain='admin')
def admin_index():
    return 'Привет, это страница администратора!'


@app.route('/bebebe')
def bebebe():
    return "Это страница bebebe"


@app.errorhandler(404)
def page_not_found(error):
    return render_template_string(f'Not found! {request.path}'), 404



if __name__ == "__main__":
    app.run(debug=True, port=9999)
