class ExpProceduresController < ApplicationController
  def index
    @procedures = Procedure.all
    @experts = Expert.all
  end

  def qwer
    return "Hello";
  end

end
