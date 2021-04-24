import User from '../domain/entities/User';
import UserRepository from '../domain/repositories/UserRepository';

export default class UserService {
    constructor(private userRepository: UserRepository) {
    }

    async add(user: User): Promise<User> {
        return this.userRepository.add(user);
    }

    async update(user: User): Promise<void> {
        return this.userRepository.update(user);
    }

    async delete(id: string): Promise<void> {
        return this.userRepository.delete(id);
    }

    async findAll(): Promise<User[]> {
        return this.userRepository.findAll();
    }

    async findById(id: string): Promise<User> {
        return this.userRepository.findById(id);
    }
}