import User from '../../domain/entities/User';
import UserRepository from '../../domain/repositories/UserRepository';
import {ApiGateway} from '../config/apiGateway';

export default class UserApiRepository implements UserRepository {
    readonly URL: string = '/users';

    constructor(private api: ApiGateway) {
    }

    async add(user: User): Promise<User> {
        return this.api.post(this.URL, user, {inType: User, outType: User});
    }

    async update(user: User): Promise<void> {
        return this.api.put(this.URL + '/' + user.id, user, {inType: User});
    }

    async delete(id: string): Promise<void> {
        return this.api.delete(this.URL + '/' + id);
    }

    async findAll(): Promise<User[]> {
        return this.api.getArray(this.URL, {outType: User});
    }

    async findById(id: string): Promise<User> {
        return this.api.get(this.URL + '/' + id, {outType: User});
    }
}