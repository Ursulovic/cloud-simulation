import { Component } from '@angular/core';
import {UserListService} from "../../services/user-list.service";
import {Router} from "@angular/router";
import {User} from "../../model";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent {

   userList : User[];
  constructor(private userListService: UserListService, private router: Router) {
    this.userList = [];
    this.getUsers();

  }
  getUsers() {
    this.userListService.getAllUsers().subscribe(res => {
      this.userList = res;
      console.log(res);
      this.userList.forEach(u => {
        u.perms = '';
        u.permissions.forEach(p => {
          u.perms = u.perms.concat(p.permission.name + ", ");

        })
      })
    })
  }

  deleteUser(id : number) {
    this.userListService.deleteUser(id).subscribe(res => {
      if (res.status == 200) {
        window.location.reload();
      }
      else {
        alert("Not permitted!");
      }
    })
  }


}
