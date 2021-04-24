import Setting from '../entities/Setting';

export default interface SettingRepository {

    add(setting: Setting): Promise<Setting>;

    update(setting: Setting): Promise<void>;

    get(): Promise<Setting>;
}