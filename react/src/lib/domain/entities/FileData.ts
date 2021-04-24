import BaseEntity from './BaseEntity';

export default class FileData extends BaseEntity {
    public path: string = '';
    public name: string = '';
    public isDirectory: boolean = false;
    public parentPath: string | null = null;
    public content: string = '';
    public childrenPathList: string[] = [];
}