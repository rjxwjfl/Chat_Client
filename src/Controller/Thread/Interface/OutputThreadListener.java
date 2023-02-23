package src.Controller.Thread.Interface;

import src.Model.DataTransferObject.Dto;

public interface OutputThreadListener{
    void onOutput(Dto<?> dto);
}
