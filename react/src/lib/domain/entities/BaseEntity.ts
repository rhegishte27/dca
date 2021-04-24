export default abstract class BaseEntity {

    toPlainObj(): any {
        return Object.assign({}, this);
    }

    applyData(json: string): void {
        Object.assign(this, json);
    }
}
