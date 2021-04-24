import BaseEntity from './BaseEntity';
import Language from './Language';

export default class Setting extends BaseEntity {
    public id: string = '';
    public language: Language = new Language();
    public tokenDuration: string = '';
    public commonFolder: string = '';
    public defaultImportFolder: string = '';
    public defaultExportFolder: string = '';
    public defaultDownloadFolder: string = '';
}