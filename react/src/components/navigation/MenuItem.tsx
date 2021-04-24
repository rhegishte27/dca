import Feature from '../../lib/domain/entities/Feature';

export interface MenuItem {
    title: string;
    features?: Feature[],
    items?: MenuItem[];
    disable?: boolean;

    onClick?(): void;
}
