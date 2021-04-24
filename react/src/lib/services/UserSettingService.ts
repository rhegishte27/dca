import UserSetting from '../domain/entities/UserSetting';
import UserSettingRepository from '../domain/repositories/UserSettingRepository';

export default class UserSettingService {
    constructor(private userSettingRepository: UserSettingRepository) {
    }

    async findById(id: string): Promise<UserSetting> {
        return this.userSettingRepository.findById(id);
    }

    async save(userSetting: UserSetting): Promise<UserSetting> {
        return this.userSettingRepository.save(userSetting);
    }

    async delete(id: string): Promise<void> {
        return this.userSettingRepository.delete(id);
    }
}