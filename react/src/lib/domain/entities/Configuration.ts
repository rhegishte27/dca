import BaseEntity from './BaseEntity';
import Language from './Language';
import User from './User';
import UserSetting from './UserSetting';

export default class Configuration extends BaseEntity {
    public user: User | undefined;
    public language: Language = new Language();
    public userSetting: UserSetting | undefined;
}
