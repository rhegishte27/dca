import User from '../entities/User';

export default interface UserRepository {
    add(user: User): Promise<User>;

    update(user: User): Promise<void>;

    delete(id: string): Promise<void>;

    findAll(): Promise<User[]>;

    findById(id: string): Promise<User>;
}