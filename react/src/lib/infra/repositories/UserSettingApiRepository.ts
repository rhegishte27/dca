import UserSetting from '../../domain/entities/UserSetting';
import UserSettingRepository from '../../domain/repositories/UserSettingRepository';
import { ApiGateway } from '../config/apiGateway';

export default class UserSettingApiRepository implements UserSettingRepository {
    readonly URL: string = '/usersettings';

    constructor(private api: ApiGateway) {
    }

    async save(userSetting: UserSetting): Promise<UserSetting> {
        return this.api.post(this.URL, userSetting, { inType: UserSetting, outType: UserSetting });
    }

    async delete(id: string): Promise<void> {
        return this.api.delete(this.URL + '/' + id);
    }

    async findById(id: string): Promise<UserSetting> {
        return this.api.get(this.URL + '/' + id, { outType: UserSetting });
    }
}