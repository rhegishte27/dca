import BaseEnum from './BaseEnum';

export default class PlatformType extends BaseEnum {
    public static WINDOWS_UNIX = new PlatformType('1', 'location:platformTypeEnum:windowsUnix');
    public static MAINFRAME = new PlatformType('2', 'location:platformTypeEnum:mainframe');
    public static AS400 = new PlatformType('3', 'location:platformTypeEnum:as400');
    public static AS400_2 = new PlatformType('4', 'location:platformTypeEnum:as400-2');
    public static VSE_BIM = new PlatformType('5', 'location:platformTypeEnum:vseBim');

    public constructor(id?: string, name?: string) {
        super(id, name);
    }
}
