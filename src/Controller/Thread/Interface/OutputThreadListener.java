package Controller.Thread.Interface;

import Model.DataTransferObject.Dto;

public interface OutputThreadListener{
    void onOutput(Dto<?> dto);
}
