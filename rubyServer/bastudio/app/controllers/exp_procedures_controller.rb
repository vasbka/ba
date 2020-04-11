class ExpProceduresController < ApplicationController
  def index
    @procedures = Procedure.all
    @experts = Expert.all
  end

  def new
    render plain: params
  end

  def destroy
    @expProcedure = ExpProcedure.find(params[:id])
    @expProcedure.destroy

    redirect_to expert_path(params[:expert_id])
  end

private
  def exp_proc_params
    params.require(:expertId, :procedures)
  end
end
