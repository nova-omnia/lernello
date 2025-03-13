export class LoginUser {
  email: string;
  password: string;

    constructor(data: FormData) {
        this.email = data.get("email") as string;
        this.password = data.get("password") as string;
    }
}