import { format } from "date-fns";
import vi from "date-fns/locale/vi";

const formatDate = (dateString) => {
    return format(new Date(dateString), "dd/MM/yyyy HH:mm", { locale: vi });
};

export default formatDate;