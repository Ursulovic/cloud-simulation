export interface LoginResponse {
  jwt: string
}

export interface User {
  id : number,
  name: string,
  surname: string,
  username: string,
  password: string,
  permissions: UserPermission[],

  perms: string
}

export interface UserPermission {
  id: number,
  permission: Permission
}

export interface Permission {
  id: number,
  name: string

}
