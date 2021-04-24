import UserSetting from '../entities/UserSetting';

export default interface UserSettingRepository {
    save(userSetting: UserSetting): Promise<UserSetting>;

    delete(id: string): Promise<void>;

    findById(id: string): Promise<UserSetting>;
}