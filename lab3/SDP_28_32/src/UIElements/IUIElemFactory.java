package UIElements;
import javafx.scene.control.Control;

public interface IUIElemFactory {
    abstract Control create(String uiElemType);
}
