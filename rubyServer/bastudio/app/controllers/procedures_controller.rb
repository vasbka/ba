class ProceduresController < ApplicationController
  def index
    @procedures = Procedure.all
    unless params[:expert_id].nil?
      @procedures = Procedure.where(:id => ExpProcedure.where(:expert_id => params[:expert_id]).map { |expProcedure| expProcedure.procedure_id  });
    end
  end

  def show
    @procedure = Procedure.find(params[:id])
  end

  def new
    @procedure = Procedure.new
  end

  def edit
    @procedure = Procedure.find(params[:id])
  end

  def create
    @procedure = Procedure.new(procedure_params)

    if @procedure.save
      redirect_to procedures_path
    else
      render 'new'
    end
  end

  def update
    @procedure = Procedure.find(params[:id])

    if @procedure.update(procedure_param)
      redirect_to @procedure
    else
      render 'edit'
    end
  end

  def destroy
    @procedure = Procedure.find(params[:id])
    @procedure.destroy

    redirect_to procedures_path
  end

  private
    def procedure_params
      params.require(:procedure).permit(:title, :description)
    end
end
