import Setting from '../domain/entities/Setting';
import SettingRepository from '../domain/repositories/SettingRepository';

export default class SettingService {
    constructor(private repository: SettingRepository) {
    }

    async add(setting: Setting): Promise<Setting> {
        return this.repository.add(setting);
    }

    async update(setting: Setting): Promise<void> {
        return this.repository.update(setting);
    }

    async get(): Promise<Setting> {
        return this.repository.get();
    }
}