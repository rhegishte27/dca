import BaseEnum from './BaseEnum';

export default class Compiler extends BaseEnum {

    public static GENERIC = new Compiler('1', "project:compilerEnum:generic");
    public static MICROFOCUS = new Compiler('2', "project:compilerEnum:microFocus");
    public static FUJITSU = new Compiler('3', "project:compilerEnum:fujitsu");
    public static VISUAL_AGE = new Compiler('4', "project:compilerEnum:visualAge");
    public static COBOL_II = new Compiler('5', "project:compilerEnum:cobolII");
    public static DOUBLE_BYTE = new Compiler('6', "project:compilerEnum:doubleByte");

    public constructor(id?: string, name?: string) {
        super(id, name);
    }
}
