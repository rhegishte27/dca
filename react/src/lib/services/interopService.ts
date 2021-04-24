export default class InteropService {

    getContextPath(): string {
        const pathNameSplit = window.location.pathname.split('/');
        let contextPath;
        if (pathNameSplit.length === 2) {
            contextPath = ""; // the context path is empty
        } else {
            contextPath = pathNameSplit[1];
        }
        return "/" + contextPath;
    }
}
