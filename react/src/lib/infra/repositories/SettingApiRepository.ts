import Setting from '../../domain/entities/Setting';
import SettingRepository from '../../domain/repositories/SettingRepository';
import {ApiGateway} from '../config/apiGateway';

export default class SettingApiRepository implements SettingRepository {
    readonly URL: string = '/settings';

    constructor(private api: ApiGateway) {
    }

    async add(setting: Setting): Promise<Setting> {
        return this.api.post(this.URL, setting, {inType: Setting, outType: Setting});
    }

    async update(setting: Setting): Promise<void> {
        return this.api.put(this.URL + '/' + setting.id, setting, {inType: Setting});
    }

    async get(): Promise<Setting> {
        return this.api.get(this.URL, {outType: Setting});
    }
}
