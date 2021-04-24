import BaseEntity from './BaseEntity';

export default class GeneratedCodeLanguage extends BaseEntity {
    public id: string = '';
    public name: string = '';

    public static isJava(input: string): boolean {
        return input.toString() === '1';
    }
}